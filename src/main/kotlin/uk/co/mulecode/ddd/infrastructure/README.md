### Infrastructure Layer:

Can talk to:

- Domain Layer: Implements the interfaces defined in the domain (e.g., repositories, event handlers).
- Application Layer (indirectly): Provides technical implementations used by the application layer.

Cannot talk to:

- Interfaces Layer: Should not handle user input/output or interact directly with controllers.
