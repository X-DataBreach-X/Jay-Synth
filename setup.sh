#!/usr/bin/env bash
# Jay-Synth setup: checks for a JDK, then downloads dependencies and builds the app.
# Usage (from the repo root):  ./setup.sh
set -euo pipefail

REQUIRED_JAVA=21
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/hellofx"

info() { echo "==> $*"; }
fail() { echo "ERROR: $*" >&2; exit 1; }

# --- 1. Find a JDK ---------------------------------------------------------
# Prefer JAVA_HOME if it's set. cygpath converts C:\... to /c/... in Git Bash on Windows.
if [[ -n "${JAVA_HOME:-}" ]]; then
    JAVA_BIN="$(cygpath -u "$JAVA_HOME" 2>/dev/null || echo "$JAVA_HOME")/bin"
    export PATH="$JAVA_BIN:$PATH"
fi

if ! command -v javac >/dev/null 2>&1; then
    echo "No JDK found. Install JDK $REQUIRED_JAVA or newer, then run this script again:"
    echo "  Windows: winget install EclipseAdoptium.Temurin.21.JDK"
    echo "  macOS:   brew install --cask temurin@21"
    echo "  Linux:   sudo apt install openjdk-21-jdk"
    echo "Or download it from https://adoptium.net/"
    exit 1
fi

# "javac 27.0.1" -> 27
JAVA_VERSION="$(javac -version 2>&1 | awk '{print $2}')"
JAVA_MAJOR="${JAVA_VERSION%%.*}"
if (( JAVA_MAJOR < REQUIRED_JAVA )); then
    fail "Found JDK $JAVA_VERSION, but Jay-Synth needs JDK $REQUIRED_JAVA or newer."
fi
info "Using JDK $JAVA_VERSION"

# --- 2. Build with the Maven Wrapper ---------------------------------------
cd "$PROJECT_DIR"
[[ -f mvnw ]] || fail "mvnw not found in $PROJECT_DIR. Run 'mvn wrapper:wrapper' there first."
chmod +x mvnw 2>/dev/null || true

info "Downloading dependencies and building (the first run can take a minute)..."
./mvnw -q clean compile

info "Setup complete! Start Jay-Synth with:  ./run.sh"
