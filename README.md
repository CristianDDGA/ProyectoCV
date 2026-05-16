# 🛒 ProyectoCV - Sistema de Facturación y Punto de Venta (POS)

Este proyecto consiste en un sistema de facturación bajo una arquitectura **Cliente-Servidor** para la materia de Computación Visual. La aplicación se ejecuta de forma local para el cliente (interfaz gráfica) y se conecta de forma remota a un servidor de base de datos PostgreSQL.

---

## 📐 Arquitectura del Proyecto (MVC + DAO)

Para mantener el código organizado, escalable y cumplir con las exigencias del docente, el proyecto utiliza el patrón de diseño **Modelo-Vista-Controlador (MVC)** asistido por el patrón **Data Access Object (DAO)**.

El código fuente está estrictamente organizado dentro del paquete base `com.mycompany.proyectocv` bajo la siguiente estructura de paquetes:

```text
☕ ProyectoCV
└── 📁 Source Packages
    └── 📦 com.mycompany.proyectocv
        │
        ├── 📄 ProyectoCV.java            (Punto de inicio del sistema y configuración estética)
        │
        ├── 📦 model                      <-- EL MODELO CLÁSICO
        │   ├── 📄 Usuario.java           (Datos de los usuarios y roles)
        │   ├── 📄 Producto.java          (Atributos de inventario)
        │   ├── 📄 Factura.java           (Encabezado general de la venta)
        │   └── 📄 DetalleFactura.java    (Los productos específicos comprados en una venta)
        │
        ├── 📦 views                      <-- LAS PANTALLAS (INTERFAZ GRÁFICA)
        │   ├── 📄 LoginView.java         (Formulario de acceso al sistema)
        │   └── 📄 VentaView.java         (Ventana del Punto de Venta / Facturación)
        │
        ├── 📦 controller                 <-- LOS INTERMEDIARIOS (LÓGICA)
        │   ├── 📄 LoginController.java   (Controla el acceso y verifica roles de usuario)
        │   └── 📄 VentaController.java   (Calcula IVA, subtotales y orquesta el flujo de cobro)
        │
        ├── 📦 daos                       <-- ACCESO A BASE DE DATOS (REQUERIMIENTO)
        │   ├── 📄 ConexionBD.java        (Gestión centralizada de la conexión a PostgreSQL)
        │   ├── 📄 ProductoDAO.java       (Operaciones SQL para CRUD de productos y stock)
        │   └── 📄 FacturaDAO.java        (Transacción SQL para guardar ventas y detalles)
        │
        └── 📦 reportes                   <-- COMPROBANTES DE IMPRESIÓN
            └── 📄 GeneradorPDF.java      (Lógica encargada de compilar y exportar con JasperReports)
