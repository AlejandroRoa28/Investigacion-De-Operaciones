# 📊 SimplexUD: Solver de Programación Lineal

Proyecto académico desarrollado en Java para resolver problemas de **Programación Lineal** mediante los métodos **Simplex** y **Simplex Dual**, incluyendo **análisis de sensibilidad**.

---

## 🎓 Información académica

- **Universidad:** Universidad Distrital Francisco José de Caldas  
- **Asignatura:** Investigación de Operaciones I  
- **Docente:** Lilian Astrid Bejarano Garzón  

---

## 👥 Integrantes

- Darly Catalina Nieto Vargas — 20231020229  
- Yuber Alejandro Bohorquez Roa — 20231020195  
- David Felipe García León — 20231020202  
- Jonnatan Camargo Camacho — 20231020204  

---

## 📌 Descripción del proyecto

Este proyecto implementa una aplicación en Java que:

- Convierte un modelo de Programación Lineal a **forma estándar**.
- Resuelve el modelo usando:
  - Método **Simplex** clásico.
  - Método **Simplex Dual**.
- Realiza **análisis de sensibilidad** sobre:
  - Recursos (lado derecho \(b\)).
  - Función objetivo.
  - Coeficientes tecnológicos (matriz \(A\)).

El enfoque es principalmente **académico**, para apoyar la comprensión de los métodos vistos en clase de Investigación de Operaciones I.

---

## 🧱 Estructura general del proyecto

Estructura de paquetes en `src/main/java/com.simplexanddualsolver`:

- `ui/`
  - `InterfazGrafica.java`
  - `ControladorUI.java`
- `model/`
  - `ModeloProblema.java`
  - `FormaEstandar.java`
- `solver/`
  - `MotorSolver.java` (interfaz común)
  - `SimplexSolver.java`
  - `DualSimplexSolver.java`
- `solution/`
  - `Solucion.java`
- `sensitivity/`
  - `AnalizadorSensibilidad.java`
  - `SensibilidadRecursos.java`
  - `SensibilidadFO.java`
  - `SensibilidadTecnologica.java`

> Nota: La estructura puede ajustarse ligeramente según evolucione la implementación, pero se mantiene alineada con el diagrama de clases planteado en el documento del proyecto.

---

## ⚙️ Funcionalidades principales

### 🔹 FormaEstandar

- Recibe el modelo \(A, b, c\) y tipos de restricción.
- Agrega variables de **holgura**, **exceso** y **artificiales** según corresponda.
- Construye la **tabla inicial** para los métodos Simplex y Simplex Dual.
- Define una **base inicial** (variables básicas de arranque).

### 🔹 SimplexSolver

- Implementa el método **Simplex clásico**:
  - Selección de **columna entrante**.
  - Selección de **fila saliente** (razón mínima).
  - Operaciones de **pivoteo**.
  - Verificación de **óptimo**.
- Devuelve una instancia de `Solucion` con:
  - Valores óptimos de las variables.
  - Valor óptimo de la función objetivo.
  - Tabla final.

### 🔹 DualSimplexSolver

- Implementa el método **Simplex Dual**.
- Utiliza la misma forma estándar y estructura de tabla, pero con criterios de factibilidad dual.
- Comparte la interfaz `MotorSolver` con `SimplexSolver`.

### 🔹 Análisis de sensibilidad

- `SensibilidadRecursos`:
  - Cálculo de **precios sombra**.
  - Rango de variación de los **recursos** \(b\).
  - Impacto en \(Z\) ante cambios en \(b\).

- `SensibilidadFO`:
  - Rango de variación de los coeficientes de la **función objetivo**.
  - Verificación de cambios de base.

- `SensibilidadTecnologica`:
  - Análisis de cambios en los coeficientes de la matriz **tecnológica** \(A\).
  - Evaluación de nuevas tecnologías / columnas.
  - Cálculo de **costos reducidos**.

---

## 📚 Propósito académico

Este proyecto tiene como objetivo:

- Aplicar los conceptos de **Programación Lineal** y **Método Simplex / Dual** vistos en clase.
- Desarrollar una solución **modular y orientada a objetos** en Java.
- Ilustrar, de forma práctica, el **análisis de sensibilidad** de un modelo de PL.

---

## 🧾 Licencia

Proyecto académico. El uso, modificación y distribución queda sujeto a las políticas de la Universidad Distrital Francisco José de Caldas y a los acuerdos del grupo de trabajo.

---
