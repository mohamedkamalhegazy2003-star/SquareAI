"""Generate all Android launcher resources from the MK Music source image.

Run with: python3 tools/icon.py
"""
from pathlib import Path

from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "app/icon-src/mk-music.webp"
RES = ROOT / "app/src/main/res"
DENSITIES = {"mdpi": 1, "hdpi": 1.5, "xhdpi": 2, "xxhdpi": 3, "xxxhdpi": 4}
FOREGROUND_DP = 108
SAFE_DP = 66
LEGACY_DP = 48
CANVAS = 192


def main() -> None:
    source = Image.open(SOURCE).convert("RGB")
    width, height = source.size
    # Crop the narrow black margin around the supplied rounded-square logo.
    logo = source.crop(
        (
            round(width * 0.064),
            round(height * 0.059),
            round(width * 0.933),
            round(height * 0.928),
        )
    ).resize((512, 512), Image.Resampling.LANCZOS)

    # The adaptive launcher foreground uses a transparent canvas and a safe-area mark.
    safe_size = round(CANVAS * SAFE_DP / FOREGROUND_DP)
    x = y = (CANVAS - safe_size) // 2
    art = logo.resize((safe_size, safe_size), Image.Resampling.LANCZOS)
    foreground = Image.new("RGBA", (CANVAS, CANVAS), (0, 0, 0, 0))
    foreground.alpha_composite(art.convert("RGBA"), (x, y))

    drawable_dir = RES / "drawable-nodpi"
    drawable_dir.mkdir(parents=True, exist_ok=True)
    foreground.save(
        drawable_dir / "mk_music_foreground.webp",
        "WEBP",
        quality=82,
        method=6,
    )

    # Keep density-specific foregrounds and pre-adaptive launcher fallbacks in sync.
    for density, scale in DENSITIES.items():
        canvas_px = round(FOREGROUND_DP * scale)
        safe_px = round(SAFE_DP * scale)
        scaled_art = logo.resize((safe_px, safe_px), Image.Resampling.LANCZOS)
        adaptive_foreground = Image.new("RGBA", (canvas_px, canvas_px), (0, 0, 0, 0))
        adaptive_foreground.alpha_composite(
            scaled_art.convert("RGBA"),
            ((canvas_px - safe_px) // 2, (canvas_px - safe_px) // 2),
        )

        out_dir = RES / f"mipmap-{density}"
        out_dir.mkdir(parents=True, exist_ok=True)
        adaptive_foreground.save(out_dir / "ic_launcher_foreground.png")

        legacy_px = round(LEGACY_DP * scale)
        legacy = logo.resize((legacy_px, legacy_px), Image.Resampling.LANCZOS)
        legacy.save(out_dir / "ic_launcher.png")
        legacy.save(out_dir / "ic_launcher_round.png")

    # Match the black surround in the supplied artwork.
    (RES / "values/colors.xml").write_text(
        '<?xml version="1.0" encoding="utf-8"?>\n'
        '<resources>\n'
        '    <color name="ic_launcher_background">#000000</color>\n'
        '</resources>\n',
        encoding="utf-8",
    )


if __name__ == "__main__":
    main()
