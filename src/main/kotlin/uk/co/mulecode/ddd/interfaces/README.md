### Interfaces Layer:

Can talk to:

- Application Layer: Calls application services to execute business use cases.
- Domain Layer (indirectly): Uses DTOs or responses created by application services, which may involve domain logic.

Cannot talk to:

- Infrastructure Layer: Should not access database implementations or technical details.
- Domain Layer directly: Does not handle domain objects directly but uses them via the application layer.
