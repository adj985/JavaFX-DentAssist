-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema dentassist
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema dentassist
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `dentassist` DEFAULT CHARACTER SET utf8 ;
USE `dentassist` ;

-- -----------------------------------------------------
-- Table `dentassist`.`diagnosis`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dentassist`.`diagnosis` (
  `iddiagnosis` INT(11) NOT NULL AUTO_INCREMENT,
  `diagnosis_name` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`iddiagnosis`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dentassist`.`expenses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dentassist`.`expenses` (
  `idexpenses` INT(11) NOT NULL AUTO_INCREMENT,
  `expense_purpose` VARCHAR(128) NOT NULL,
  `amount_spent` DOUBLE NOT NULL,
  `expense_date` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`idexpenses`))
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dentassist`.`patients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dentassist`.`patients` (
  `idpatients` INT(11) NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `ic_number` VARCHAR(13) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `residence` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `warning_note` VARCHAR(1000) NULL DEFAULT NULL,
  PRIMARY KEY (`idpatients`))
ENGINE = InnoDB
AUTO_INCREMENT = 48
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dentassist`.`interventions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dentassist`.`interventions` (
  `idinterventions` INT(11) NOT NULL AUTO_INCREMENT,
  `tooth` INT(2) NOT NULL,
  `description` TEXT NOT NULL,
  `date_of_intervention` VARCHAR(10) NOT NULL,
  `price` DOUBLE NOT NULL,
  `charged` VARCHAR(2) NOT NULL,
  `idpatients` INT(11) NOT NULL,
  `iddiagnosis` INT(11) NOT NULL,
  PRIMARY KEY (`idinterventions`),
  INDEX `fk_interventions_patients1_idx` (`idpatients` ASC),
  INDEX `fk_interventions_diagnosis1_idx` (`iddiagnosis` ASC),
  CONSTRAINT `fk_interventions_diagnosis1`
    FOREIGN KEY (`iddiagnosis`)
    REFERENCES `dentassist`.`diagnosis` (`iddiagnosis`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_interventions_patients1`
    FOREIGN KEY (`idpatients`)
    REFERENCES `dentassist`.`patients` (`idpatients`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 35
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dentassist`.`scheduling`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dentassist`.`scheduling` (
  `idscheduling` INT(11) NOT NULL AUTO_INCREMENT,
  `scheduled_date` VARCHAR(10) NOT NULL,
  `scheduled_time` VARCHAR(5) NOT NULL,
  `idpatients` INT(11) NOT NULL,
  PRIMARY KEY (`idscheduling`),
  INDEX `fk_scheduling_patients_idx` (`idpatients` ASC),
  CONSTRAINT `fk_scheduling_patients`
    FOREIGN KEY (`idpatients`)
    REFERENCES `dentassist`.`patients` (`idpatients`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `dentassist`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dentassist`.`users` (
  `iduser` INT(11) NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`iduser`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
