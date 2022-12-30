# Minestom Disguises

This intended to be a library/extension for disguising players/other entities in [Minestom](https://github.com/Minestom/Minestom).

Currently *very* work in progress, feedback, ideas, and contributions are appreciated!

# Table of Contents

- [Goals](#goals)
- [TODOs](#todo)
- [Usage](#usage)

# Goals

The goals are to create a library that can be imported, and a standalone extension, that can be used to disguise players and other entities
as whatever entities you desire, and manipulate those disguises to your heart's content.

# TODO

- More translation of Player -> Disguise with events and packets
- Modify disguises after creation
- Properly unload things when disguised players log out
- Config setting to view/hide disguise to self

and more...

# Usage

Currently, this project is very early in development and is nowhere near complete, but the very basics can be tested by compiling it as
an extension using `./gradlew shadowJar` and running the extension in a Minestom server.

You can then use /disguise [player] and /undisguise [player] to have a simple disguise follow you around or remove it.

# License

This project is licensed under the [MIT License](./LICENSE).
