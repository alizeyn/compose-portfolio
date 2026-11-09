# Ali Zeynali — personal site, in Compose

The personal site rebuilt as a **Compose Multiplatform for Web** app: Kotlin compiled
to WebAssembly, drawn with **Material 3** components, adaptive layout and a
light/dark colour scheme seeded from the printed CV poster palette.

```bash
./gradlew wasmJsBrowserDevelopmentRun      # dev server with live reload
./gradlew wasmJsBrowserDistribution        # -> composeApp/build/dist/wasmJs/productionExecutable
```

Needs JDK 17+ (the build uses 21 in CI). On this Mac, Android Studio's bundled JDK works:

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
```

## Structure

```
composeApp/src/
  commonMain/kotlin/com/alizeyn/site/
    App.kt                  adaptive shell: NavigationBar (compact) / NavigationRail (medium+)
    data/Cv.kt              all copy, in one place
    theme/                  M3 colour schemes, shape scale, type scale
    ui/Common.kt            Page container, section header, banner, accent paragraphs
    ui/*Screen.kt           Home, About, Work, FAQ, Contact
  commonMain/composeResources/drawable/   photographs and vector icons
  wasmJsMain/kotlin/.../main.kt           ComposeViewport entry point
  wasmJsMain/resources/   index.html shell, styles.css, favicon
.github/workflows/deploy.yml   builds the Wasm bundle and deploys it to GitHub Pages
```

## Material 3 choices

- **Window size classes** at 600dp and 840dp decide the navigation component
  (bar vs rail), the layout margins (16dp vs 24dp) and one- vs two-column heroes.
- **Colour**: hand-built light and dark `ColorScheme`s. Primary is the poster's
  cobalt, secondary its teal, tertiary the rust/orange, background the paper tone.
  Company accents on the Work page use tonal container pairs that follow the theme.
- **Shape** uses the M3 shape-scale tokens; photographs and hero cards sit on
  `extraLarge` (28dp).
- **Type** is the default M3 type scale with slightly heavier display/headline roles.
- Components used: TopAppBar, NavigationBar/Rail, FAB and Extended FAB, Button,
  FilledTonalButton, OutlinedButton, Assist/Suggestion chips, Elevated/Outlined/Filled
  cards, ListItem, HorizontalDivider.

## Deploying

Pushing to `main` runs the workflow, which builds the distribution and publishes it to
GitHub Pages. The output is static files (`index.html`, `composeApp.js`,
`composeApp.wasm`, `skiko.*`), so it can equally be served from any static host.

Because the whole page is drawn on a canvas, search engines and link previews only see
`index.html`: keep its `<title>`, `<meta name="description">` and Open Graph tags current.
