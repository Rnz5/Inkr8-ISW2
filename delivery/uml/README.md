# UML final editable

Fuentes PlantUML `.puml`, imágenes `.png` y `.svg` del código final. Son modelos
selectivos de Codex, no una revisión humana acreditada. `../source-map.json`
vincula mensajes con métodos, líneas y SHA de producción para P4 §8.1.2.5.
El modelo de datos describe rutas y referencias Firestore; no inventa claves SQL.
La vista de despliegue representa el laboratorio realmente utilizado y señala
servicios externos no comprobados. PlantUML es la herramienta especializada.

Reproducir con una instalación oficial de PlantUML y JDK17:
`java -jar plantuml.jar -tsvg delivery/uml/*.puml`
y `java -jar plantuml.jar -tpng delivery/uml/*.puml`.
La versión usada y su hash se registran en `tool.json`; el JAR no se entrega.
