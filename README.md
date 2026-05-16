Arquitectura de la aplicacion

☕ ProyectoCV
└── 📁 Source Packages
    └── 📦 com.mycompany.proyectocv
        │
        ├── 📄 ProyectoCV.java            (Main)
        │
        ├── 📦 model                      <-- EL MODELO CLÁSICO
        │   ├── 📄 Usuario.java
        │   ├── 📄 Producto.java
        │   ├── 📄 Factura.java
        │   └── 📄 DetalleFactura.java
        │
        ├── 📦 views                      <-- LAS PANTALLAS
        │   ├── 📄 LoginView.java
        │   └── 📄 VentaView.java
        │
        ├── 📦 controller                 <-- LOS INTERMEDIARIOS
        │   ├── 📄 LoginController.java
        │   └── 📄 VentaController.java
        │
        ├── 📦 daos                       <-- BASE DE DATOS (Requerimiento del profe)
        │   ├── 📄 ConexionBD.java
        │   ├── 📄 ProductoDAO.java
        │   └── 📄 FacturaDAO.java
        │
        └── 📦 reportes                   <-- FACTURAS
            └── 📄 GeneradorPDF.java
