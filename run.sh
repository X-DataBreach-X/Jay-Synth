#!/usr/bin/env bash
# Starts Jay-Synth. Run ./setup.sh once first.
set -euo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")/hellofx"
exec ./mvnw -q javafx:run
