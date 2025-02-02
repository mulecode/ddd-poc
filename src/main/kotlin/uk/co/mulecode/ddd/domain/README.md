### Domain Layer:

Can talk to:

- Itself: The domain layer is self-contained and doesn’t depend on other layers.
- Aggregates, Value Objects, Domain Events, and Repositories are used internally.

Cannot talk to:

- Application Layer: The domain must not depend on how the use cases are orchestrated.
- Infrastructure Layer: The domain knows only the repository interfaces, not their implementations.
- Interfaces Layer: No direct awareness of controllers or external systems.
