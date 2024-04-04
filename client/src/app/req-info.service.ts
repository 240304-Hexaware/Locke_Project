import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ReqInfoService {
  private msgSpeed = 4000
  info = new Map<String, boolean>([
    ["loggingIn", false],
    ["loginFailed", false],
    
    ["uploadingSpec", false],
    ["specUploadSuccess", false],
    ["specUploadFail", false],
    ["uploadingFlat", false],
    ["flatUploadSuccess", false],
    ["flatUploadFail", false],

    ["invalidSpec", false],
    ["invalidFlat", false],

    ["existingFile", false],
  ])

  get loggingIn() { return this.info.get("loggingIn") }
  logIn() {this.info.set("loggingIn", true)}

  logInSucceeded() {
    this.info.set("loggingIn", false)
  }

  get loginFailed() { return this.info.get("loginFailed")}
  failLogin() {
    this.info.set("loggingIn", false)
    this.info.set("loginFailed", true)
    setTimeout(() => this.info.set("loginFailed", false), this.msgSpeed)
  }

  constructor() { }

  get flatUploaded(): boolean { return this.info.get("isFlatUploading")! }
  get specUploaded(): boolean { return this.info.get("isSpecUploading")!}

  get flatUploadSuccessful(): boolean { return this.info.get("flatUploadSuccess")! }
  get specUploadedSuccessful(): boolean { return this.info.get("specUploadSuccess")! }
  
  succeededFlatUpload() {
    this.info.set("flatUploadSuccess", true)
    this.info.set("uploadingFlat", false)
    
    setTimeout(() => this.info.set("flatUploadSuccess", false), this.msgSpeed)
  }

  specUploading() { this.info.set("uploadingSpec", true) }
  succeededSpecUpload() {
    this.info.set("specUploadSuccess", true)
    this.info.set("uploadingSpec", false)
    setTimeout(() => this.info.set("specUploadSuccess", false), this.msgSpeed)
  }
  failedSpecUpload() {
    console.log("Hello")
    this.info.set("specUploadFail", true)
    this.info.set("uploadingSpec", false)
    setTimeout(() => this.info.set("specUploadFail", false), this.msgSpeed)
  }

  get flatUploadFailed(): boolean { return this.info.get("flatUploadFail")! }
  get specUploadFailed(): boolean { return this.info.get("specUploadFail")! }

  failedFlatUpload() {
    this.info.set("flatUploadFail", true)
    this.info.set("uploadingFlat", false)
    setTimeout(() => this.info.set("flatUploadFail", false), this.msgSpeed)
  }

  get invalidFlatAttempt() { return this.info.get("invalidFlat")! }

  attemptInvalidFlatUpload() {
    this.info.set('invalidFlat', true)
    setTimeout(() => this.info.set("invalidFlat", false), this.msgSpeed);
  }

  get existingFile() { return this.info.get("existingFile")}
  fileAlreadyExists() {
    this.info.set("existingFile", true)
    setTimeout(() => this.info.set("existingFile", false), this.msgSpeed)
  }
}
