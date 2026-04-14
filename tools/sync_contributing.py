from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "CONTRIBUTING.md"
TARGET = ROOT / "docs" / "contributing.md"
HEADER = "<!-- Generated from CONTRIBUTING.md. Do not edit directly. -->\n\n"


def main() -> None:
    content = SOURCE.read_text(encoding="utf-8").replace("\r\n", "\n").rstrip() + "\n"
    TARGET.write_text(HEADER + content, encoding="utf-8", newline="\n")
    print(f"Synced {TARGET.relative_to(ROOT)} from {SOURCE.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
