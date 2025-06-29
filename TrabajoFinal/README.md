# Calculadora Estadística

Esta es una aplicación de escritorio desarrollada en Java que permite realizar análisis estadísticos básicos.

## Requisitos

- Java 11 o superior
- Maven
- IntelliJ IDEA (recomendado)

## Instrucciones de instalación

1. Clona o descarga este repositorio
2. Abre el proyecto en IntelliJ IDEA
3. Espera a que Maven descargue las dependencias
4. Ejecuta la clase `CalculadoraEstadistica` desde el método `main`

## Características

- Interfaz gráfica con tabla de datos
- Exportación e importación de datos en formato CSV
- Análisis estadísticos básicos
- Transformación de datos
- Configuración personalizable

## Estructura del proyecto

```
calculadora-estadistica/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── calculadora/
│                   └── CalculadoraEstadistica.java
├── pom.xml
└── README.md
```

## Uso

1. La aplicación se inicia con una tabla de datos de ejemplo
2. Puedes modificar los datos directamente en la tabla
3. Usa los botones de la barra de herramientas para:
   - Vaciar la tabla
   - Exportar/Importar datos en formato CSV
   - Transformar datos (en desarrollo)
   - Configuración (en desarrollo)
4. Las pestañas inferiores permiten realizar diferentes tipos de análisis estadísticos 