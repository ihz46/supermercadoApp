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

-- Volcando estructura para tabla supermercado.producto
CREATE TABLE IF NOT EXISTS `producto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `precio` float NOT NULL DEFAULT '0',
  `imagen` varchar(150) NOT NULL,
  `descripcion` varchar(150) NOT NULL,
  `descuento` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Volcando datos para la tabla supermercado.producto: ~3 rows (aproximadamente)
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` (`id`, `nombre`, `precio`, `imagen`, `descripcion`, `descuento`) VALUES
	(1, 'leche', 2, 'https://www.lavanguardia.com/r/GODO/LV/p5/WebSite/2018/11/05/Recortada/img_jperezf_20180723-154950_imagenes_lv_otras_fuentes_fotonoticia_2018072013240', 'de cabra', 0),
	(2, 'cafe', 1.3, 'https://www.baque.com/wp-content/uploads/2018/11/26115014-capsulas-la-coleccion-ecommerce-14uds-intenso-varanasi.jpg', 'mucha cafeina', 0),
	(3, 'tortilla', 1.2, 'https://s4.eestatic.com/2019/10/30/cocinillas/recetas/aperitivos-y-entrantes/Tortilla-Patatas-Tortilla_de_patata-Recetas_Rapidas-Videorreceta-Aperitiv', 'la buena tortilla', 0),
	(4, 'queso', 34, 'https://quesospeinaovejas.com/119-large_default/queso-iberico-de-oveja-con-jamon-cuna.jpg', 'manchego', 0);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
