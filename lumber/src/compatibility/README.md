# Released consumers

These are test fixtures, not library classes. The convention compiles each Java source against
a released Maven artifact, then runs the unchanged bytecode against the candidate publication.
Ordinary common tests compile against the candidate and cannot detect missing old JVM descriptors.

`Consumer` protects the API before 1.2.0, including protected subclass getters. `CurrentConsumer`
protects the current facade return types. The convention selects the latest stable release tag
within each API generation, excluding the candidate version. Today these are 1.1.0 and 1.4.4.
Java keeps this test independent of a second Kotlin build.
