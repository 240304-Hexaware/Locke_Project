import { Component } from '@angular/core';
import { UserDataService } from '../user-data.service';
import { RouterLink } from '@angular/router';
import { NgIf  } from '@angular/common';
@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [NgIf, RouterLink],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css'
})
export class NavBarComponent {
  userDataService: UserDataService

  constructor(userDataService: UserDataService) {
    this.userDataService = userDataService
  }


}
