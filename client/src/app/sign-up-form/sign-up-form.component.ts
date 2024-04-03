import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UserDataService } from '../user-data.service';

@Component({
  selector: 'app-sign-up-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './sign-up-form.component.html',
  styleUrl: './sign-up-form.component.css'
})
export class SignUpFormComponent {
  userDataService: UserDataService
  roles = ["User", "Admin"]

  userData = new FormGroup({
    username: new FormControl<string>("", {nonNullable: true}),
    password: new FormControl<string>("", {nonNullable: true}),
    role: new FormControl<string>("User", {nonNullable: true})
  })

  constructor(userDataService: UserDataService) {
    this.userDataService = userDataService
  }

  onSubmit() {
    this.userDataService.signup(this.userData.getRawValue())
  }

}
