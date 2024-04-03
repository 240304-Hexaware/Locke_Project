import { Component } from '@angular/core';
import { ReqInfoService } from '../req-info.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [NgIf],
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.css'
})
export class NotificationComponent {
  reqInfoService: ReqInfoService

  constructor(reqInfoService: ReqInfoService) {
    this.reqInfoService = reqInfoService
  }
}
