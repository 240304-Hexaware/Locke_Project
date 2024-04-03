import { Component } from '@angular/core';
import { UserDataService } from '../user-data.service';
import { NgIf } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { ReqInfoService } from '../req-info.service';


@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css'
})
export class LoginFormComponent {
  userDataService: UserDataService
  reqInfoService: ReqInfoService

  failedLogin = false

  userData = new FormGroup({
    username: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required
      ]
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(6)
      ]
    })
  })

  constructor(userDateService: UserDataService, reqInfoService: ReqInfoService) {
    this.userDataService = userDateService
    this.reqInfoService = reqInfoService
  }

  onSubmit() {
    if(!this.reqInfoService.loggingIn) {
      this.reqInfoService.logIn()
      this.userDataService.login(this.userData.getRawValue())
    }
  }

  get username() {
    return this.userData.get('username')
  }

  get password() {
    return this.userData.get('password')
  }

}
