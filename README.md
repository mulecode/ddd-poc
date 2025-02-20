# ddd-poc

An example spring boot application structured using domain driven design

# Installing Next

```shell
npx --version
11.1.0
```

```shell
which npx
/Users/***/Library/Application Support/JetBrains/IntelliJIdea2024.3/node/versions/22.13.1/bin/npx
```

Aparently already installed with IntelliJ IDEA

```shell
npx create-next-app@latest --ts
```

next.config.ts add the base path to the app:

```javascript
const nextConfig: NextConfig = {
    basePath: '/app',
    /* other config options */
};
```

to run:
```shell
npm run dev
```

to build:

```shell
npm run build
```

it will create a .next folder with the build files
