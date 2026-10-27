#!/usr/bin/env python3
"""Generate the MK Music adaptive launcher icon from the bundled source image."""
from pathlib import Path

from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "app/icon-src/mk-music.webp"
RES = ROOT / "app/src/main/res"
CANVAS = 192
SAFE_RATIO = 66 / 108


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

    safe_size = round(CANVAS * SAFE_RATIO)
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

    # Match the black surround in the supplied logo.
    (RES / "values/colors.xml").write_text(
        '<?xml version="1.0" encoding="utf-8"?>\n'
        '<resources>\n'
        '    <color name="ic_launcher_background">#000000</color>\n'
        '</resources>\n',
        encoding="utf-8",
    )


if __name__ == "__main__":
    main()
