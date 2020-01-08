-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         5.5.24-log - MySQL Community Server (GPL)
-- SO del servidor:              Win64
-- HeidiSQL Versión:             10.3.0.5771
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Volcando estructura de base de datos para supermercado
CREATE DATABASE IF NOT EXISTS `supermercado` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `supermercado`;

-- Volcando estructura para tabla supermercado.categoria
CREATE TABLE IF NOT EXISTS `categoria` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla supermercado.categoria: ~14 rows (aproximadamente)
DELETE FROM `categoria`;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` (`id`, `nombre`) VALUES
	(10, 'basicos'),
	(9, 'bolleria'),
	(3, 'carne'),
	(7, 'electrodomesticos'),
	(6, 'electronica'),
	(12, 'embutido de cerdo'),
	(1, 'frescos'),
	(2, 'fruta'),
	(5, 'hogar'),
	(4, 'limpieza'),
	(11, 'menaje'),
	(8, 'panaderia'),
	(13, 'prueba1'),
	(22, 'pruebita');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;

-- Volcando estructura para tabla supermercado.producto
CREATE TABLE IF NOT EXISTS `producto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `precio` float NOT NULL DEFAULT '0',
  `imagen` varchar(150) NOT NULL,
  `descripcion` varchar(150) NOT NULL,
  `descuento` int(11) NOT NULL DEFAULT '0',
  `id_usuario` int(11) NOT NULL,
  `id_categoria` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`),
  KEY `fk_producto_usuario` (`id_usuario`),
  KEY `fk_producto_categoria` (`id_categoria`),
  CONSTRAINT `fk_producto_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id`),
  CONSTRAINT `fk_producto_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla supermercado.producto: ~10 rows (aproximadamente)
DELETE FROM `producto`;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` (`id`, `nombre`, `precio`, `imagen`, `descripcion`, `descuento`, `id_usuario`, `id_categoria`) VALUES
	(1, 'leche', 2, 'https://images-na.ssl-images-amazon.com/images/I/416Ik-HVV1L.jpg', 'de cabra', 0, 1, 10),
	(2, 'cafe', 1.3, 'https://www.baque.com/wp-content/uploads/2018/11/26115014-capsulas-la-coleccion-ecommerce-14uds-intenso-varanasi.jpg', 'mucha cafeina', 0, 1, 1),
	(3, 'tortilla', 1.2, 'https://a0.soysuper.com/ea5cf12e0d8cf9d13c05804aec1ed8c8.1500.0.0.0.wmark.4dc90424.jpg', 'la buena tortilla', 0, 1, 1),
	(4, 'queso', 34, 'https://quesospeinaovejas.com/119-large_default/queso-iberico-de-oveja-con-jamon-cuna.jpg', 'manchego', 0, 1, 1),
	(5, 'fanta naranja', 1.2, 'http://sushimore.com/wp-content/uploads/2019/02/fantanaranjamore.jpg', 'con mucho gas', 10, 4, 1),
	(7, 'bacon ahumado', 2, 'https://www.supermercadosmas.com/media/catalog/product/cache/e4d64343b1bc593f1c5348fe05efa4a6/i/m/import_catalog_images_01_40_014001_v8_7.jpg', 'oferta', 50, 9, 1),
	(8, 'bacon', 2.2, 'https://image.flaticon.com/icons/png/512/372/372627.png', 'ekesokeo', 10, 9, 1),
	(9, 'sal', 1.2, 'https://image.flaticon.com/icons/png/512/372/372627.png', 'eofkeokef ', 0, 9, 1),
	(11, 'mandarinas', 1.2, 'https://image.flaticon.com/icons/png/512/372/372627.png', 'okekoedkedok', 20, 9, 2),
	(12, 'minigalletitas', 2.1, 'https://image.flaticon.com/icons/png/512/372/372627.png', 'malisimas', 20, 9, 10);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;

-- Volcando estructura para tabla supermercado.rol
CREATE TABLE IF NOT EXISTS `rol` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla supermercado.rol: ~2 rows (aproximadamente)
DELETE FROM `rol`;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` (`id`, `nombre`) VALUES
	(1, 'usuario'),
	(2, 'admin');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;

-- Volcando estructura para tabla supermercado.usuario
CREATE TABLE IF NOT EXISTS `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL DEFAULT '0',
  `password` varchar(50) NOT NULL DEFAULT '0',
  `id_rol` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`),
  KEY `fk_rol` (`id_rol`),
  CONSTRAINT `fk_rol` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla supermercado.usuario: ~6 rows (aproximadamente)
DELETE FROM `usuario`;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`id`, `user_name`, `password`, `id_rol`) VALUES
	(1, 'admin', '123456', 2),
	(4, 'jorge lorenzo', 'jorgeiii', 1),
	(5, 'angelon', 'angel', 1),
	(7, 'administrador', 'admin', 2),
	(8, 'felix', 'felix1234', 2),
	(9, 'pepita', '123456', 1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;

-- Volcando estructura para procedimiento supermercado.pa_categoria_delete
DELIMITER //
CREATE PROCEDURE `pa_categoria_delete`(
	IN `p_id` INT
)
    COMMENT 'Procedimiento almacenado para eliminar una categoria.'
BEGIN
	DELETE FROM categoria WHERE id = p_id;

END//
DELIMITER ;

-- Volcando estructura para procedimiento supermercado.pa_categoria_getall
DELIMITER //
CREATE PROCEDURE `pa_categoria_getall`()
    COMMENT 'procedimiento que devolverá todas las categorias de la bd supermercado'
BEGIN
	-- Nuestro primer Procedimiento Almacenado
	SELECT id, nombre FROM categoria ORDER BY nombre ASC LIMIT 500;
END//
DELIMITER ;

-- Volcando estructura para procedimiento supermercado.pa_categoria_get_by_id
DELIMITER //
CREATE PROCEDURE `pa_categoria_get_by_id`(
	IN `p_id` INT
)
    COMMENT 'Procedimiento almacenado para obtener una categoria por su id'
BEGIN

	SELECT id, nombre FROM categoria WHERE id = p_id;

END//
DELIMITER ;

-- Volcando estructura para procedimiento supermercado.pa_categoria_insert
DELIMITER //
CREATE PROCEDURE `pa_categoria_insert`(
	IN `p_nombre` VARCHAR(100),
	OUT `p_id` INT
)
    COMMENT 'procedimiento almacenado para crear una nueva categoría, parámetros vamos a pasar el nombre.'
BEGIN

 -- Crear el nuevo registro
	INSERT INTO categoria(nombre) VALUES(p_nombre);
	
 -- Obtener el ID generado
 	SET p_id = LAST_INSERT_ID();
 	
 -- retornar el ID 
END//
DELIMITER ;

-- Volcando estructura para procedimiento supermercado.pa_categoria_update
DELIMITER //
CREATE PROCEDURE `pa_categoria_update`(
	IN `p_nombre` VARCHAR(100),
	IN `p_id` INT
)
    COMMENT 'Procedimiento almacenado al que le pasamos un id como parámetro de entrada y actualiza una categoría'
BEGIN
	UPDATE categoria SET nombre = p_nombre WHERE id = p_id;
END//
DELIMITER ;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
