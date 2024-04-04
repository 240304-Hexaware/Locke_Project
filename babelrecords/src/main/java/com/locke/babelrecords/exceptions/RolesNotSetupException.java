package com.locke.babelrecords.exceptions;

public class RolesNotSetupException extends Exception {
  public RolesNotSetupException() {
    super("Roles not setup before trying to register user. Or, tried to access invalid role.");
  }
}
