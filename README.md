## The Disaster Relief System
<< A demonstration of my knowledge in object oriented programming, PostgreSQL, and test driven development. >>

This repository is for the final project of my university object oriented programming course. This final project consisted of a large design problem requiring the development - from scratch - of a "disaster relief system" coded in Java, that allowed users (in theory, medical staff) to input, modify, remove, and view information pertaining to victims of a disaster, where they are sheltered, personal information, etc.

Development began with the writing of unit tests for each class that was planned to be implemented. Unit tests were ran using JUnit, and concrete code was written based on these unit tests. A process of test, code, refactor, repeat was followed where unit tests could be ran (which was in most cases, other than in the simplest of functionalities).

Information handling - pertaining to disaster victim information, inquiries, shelter information, etc. - was all sent to and received from an actual database handled using PostgreSQL. Java-PostgreSQL "bridging" was done using JDBC with classes that specifically handled database communication and modification - so called "Access" classes. 

Object oriented programming principles were heavily leveraged as well, with the use of Singleton patterns implemented with enums, Observer patterns implemented with a classic model-view-controller structure with Access classes, encapsulation and abstraction implemented with heavily modular classes, and much more. All culminating in a challenging yet rewarding effort to push my limits of what I know about true software design and implementing best design practices.

All in all, this project was a fantastic application of my "book knowledge" of OOP, PostgreSQL, and TDD. It pushed me to my limits of applying course content to a real, practical application with concrete code to demonstrate my learning. 
