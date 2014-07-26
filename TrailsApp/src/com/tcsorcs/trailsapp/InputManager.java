package com.tcsorcs.trailsapp;

class InputManager {
  public static InputManager getInstance() {
	  return InputManager.instance;
  }
  static InputManager instance = new InputManager();
}
