import { Component } from '@angular/core';
import { UserDataService } from '../user-data.service';
import { NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-history-view',
  standalone: true,
  imports: [NgFor, NgIf],
  templateUrl: './history-view.component.html',
  styleUrl: './history-view.component.css'
})
export class HistoryViewComponent {
  userDataService: UserDataService

  constructor(userDataService: UserDataService) {
    this.userDataService = userDataService
    this.userDataService.getHistory()
  }

}
