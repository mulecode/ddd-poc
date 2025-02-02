### Application Layer:

Can talk to:

- Domain Layer: Uses domain models, repositories, and events to execute business use cases.
- Infrastructure Layer (indirectly): Accesses the implementations (e.g., JpaUserRepository) through domain-defined
  interfaces.

Cannot talk to:

- Interfaces Layer: Does not directly handle user input/output or external requests.
