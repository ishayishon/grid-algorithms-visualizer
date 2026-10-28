# Changelog

## Portfolio maintenance 2026 10 28

- Preserved the original coursework separately and recorded the SHA-256 fingerprints of its six Java files.
- Fixed the Fill panels so the grid is visible before the first click and controls remain after filling.
- Added Reset controls to repeat the demonstration without restarting the application.
- Returned two-point paths in start-to-destination order.
- Updated multi-stop route construction to use that orientation and concatenate legs without deleting an earlier occurrence of a target.
- Returned `null` for invalid or disconnected multi-target requests, as specified by `Map2D`.
- Updated one legacy exception assertion to match that documented contract, and replaced a constant-versus-constant assertion with an actual closest-pair check.
- Used `ArrayDeque` for the BFS queues and avoided reconstructing a path when only its distance is required.
- Retained the original course interfaces and scaffold attribution.
- Added ten independent regression/property tests, a desktop smoke check, launch scripts, a Maven build, and screenshots.

The maintenance and packaging work was completed with AI assistance. These updates are distinguished from the original student submission; the project remains an academic algorithm visualizer.

## Verification

Tested on Windows with OpenJDK 21.0.2. All 28 unit/regression tests passed. The desktop check visited all 11 tabs, confirmed 420 visible cells in each grid, and exercised repeated filling and resetting in both cyclic and bounded modes. Randomized tests use fixed seeds for reproducibility. These checks cover the tested inputs and UI paths; they are not a proof of correctness for every possible input.
