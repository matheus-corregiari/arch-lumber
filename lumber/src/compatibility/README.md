# Released consumers

These are test fixtures, not library classes. The convention compiles each Java source against
a released Maven artifact, then runs the unchanged bytecode against the candidate publication.
Ordinary common tests compile against the candidate and cannot detect missing old JVM descriptors.

`Consumer` protects the 1.1.0 public methods and protected subclass getters. `CurrentConsumer`
protects the 1.4.4 facade return types. Java keeps this test independent of a second Kotlin build.
