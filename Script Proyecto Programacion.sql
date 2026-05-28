CREATE SCHEMA IF NOT EXISTS `sistemadepaqueteria`
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;

USE `sistemadepaqueteria`;

-- Tabla Rol
CREATE TABLE IF NOT EXISTS `Rol` (
  `idRol` INT NOT NULL AUTO_INCREMENT,
  `nomRol` VARCHAR(45) NULL,
  PRIMARY KEY (`idRol`)
) ENGINE = InnoDB;

-- Tabla Usuario
CREATE TABLE IF NOT EXISTS `Usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  `apellido` VARCHAR(45) NULL,
  `correo` VARCHAR(45) NULL,
  `contrasenia` VARCHAR(45) NULL,
  `Rol_idRol` INT NOT NULL,
  PRIMARY KEY (`idUsuario`),
  INDEX (`Rol_idRol`),
  CONSTRAINT `fk_Usuario_Rol`
    FOREIGN KEY (`Rol_idRol`)
    REFERENCES `Rol` (`idRol`)
) ENGINE = InnoDB;

-- Tabla Cliente
CREATE TABLE IF NOT EXISTS `Cliente` (
  `idCliente` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido` VARCHAR(45) NULL,
  `telefono` VARCHAR(45) NULL,
  `direccion` VARCHAR(45) NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idCliente`),
  INDEX (`Usuario_idUsuario`),
  CONSTRAINT `fk_Cliente_Usuario`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `Usuario` (`idUsuario`)
) ENGINE = InnoDB;

-- Tabla Paquete
CREATE TABLE IF NOT EXISTS `Paquete` (
  `idPaquete` INT NOT NULL AUTO_INCREMENT,
  `numSeguimiento` VARCHAR(45) NOT NULL,
  `peso` DECIMAL(8,2) NULL,
  `tipo_envio` VARCHAR(45) NULL,
  `direccionEntrega` VARCHAR(100) NULL,
  `estadoAct` ENUM('Registrado', 'En transito', 'Entregado') NOT NULL DEFAULT 'Registrado',
  `fechaRegistro` DATETIME NOT NULL,
  `Cliente_idCliente` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idPaquete`),
  UNIQUE (`numSeguimiento`),
  INDEX (`Cliente_idCliente`),
  INDEX (`Usuario_idUsuario`),
  CONSTRAINT `fk_Paquete_Cliente`
    FOREIGN KEY (`Cliente_idCliente`)
    REFERENCES `Cliente` (`idCliente`),
  CONSTRAINT `fk_Paquete_Usuario`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `Usuario` (`idUsuario`)
) ENGINE = InnoDB;

-- Tabla Despacho
CREATE TABLE IF NOT EXISTS `Despacho` (
  `idDespacho` INT NOT NULL AUTO_INCREMENT,
  `fechaDespacho` DATETIME NOT NULL,
  `observaciones` VARCHAR(100) NULL,
  `Paquete_idPaquete` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idDespacho`),
  INDEX (`Paquete_idPaquete`),
  INDEX (`Usuario_idUsuario`),
  CONSTRAINT `fk_Despacho_Paquete`
    FOREIGN KEY (`Paquete_idPaquete`)
    REFERENCES `Paquete` (`idPaquete`),
  CONSTRAINT `fk_Despacho_Usuario`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `Usuario` (`idUsuario`)
) ENGINE = InnoDB;

-- Tabla Entrega
CREATE TABLE IF NOT EXISTS `Entrega` (
  `idEntrega` INT NOT NULL AUTO_INCREMENT,
  `fechaEntrega` DATETIME NOT NULL,
  `nombreReceptor` VARCHAR(100) NOT NULL,
  `observaciones` VARCHAR(100) NULL,
  `Paquete_idPaquete` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idEntrega`),
  INDEX (`Paquete_idPaquete`),
  INDEX (`Usuario_idUsuario`),
  CONSTRAINT `fk_Entrega_Paquete`
    FOREIGN KEY (`Paquete_idPaquete`)
    REFERENCES `Paquete` (`idPaquete`),
  CONSTRAINT `fk_Entrega_Usuario`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `Usuario` (`idUsuario`)
) ENGINE = InnoDB;

-- Tabla HistorialEstado
CREATE TABLE IF NOT EXISTS `HistorialEstado` (
  `idHistorial` INT NOT NULL AUTO_INCREMENT,
  `estado` ENUM('Registrado', 'En transito', 'Entregado') NOT NULL,
  `fechaCambio` DATETIME NOT NULL,
  `Paquete_idPaquete` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  PRIMARY KEY (`idHistorial`),
  INDEX (`Paquete_idPaquete`),
  INDEX (`Usuario_idUsuario`),
  CONSTRAINT `fk_Historial_Paquete`
    FOREIGN KEY (`Paquete_idPaquete`)
    REFERENCES `Paquete` (`idPaquete`),
  CONSTRAINT `fk_Historial_Usuario`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `Usuario` (`idUsuario`)
) ENGINE = InnoDB;