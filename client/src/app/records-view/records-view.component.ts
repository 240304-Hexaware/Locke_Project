import { Component } from '@angular/core';
import { UserDataService } from '../user-data.service';
import { NgFor, NgIf } from '@angular/common';
import { FilesService } from '../files.service';

@Component({
  selector: 'app-records-view',
  standalone: true,
  imports: [NgFor, NgIf],
  templateUrl: './records-view.component.html',
  styleUrl: './records-view.component.css'
})
export class RecordsViewComponent {
  userDataService: UserDataService
  filesService: FilesService

  filteredRecords: any[] = []

  constructor(userDataService: UserDataService, filesService: FilesService) {
    this.userDataService = userDataService
    this.filesService = filesService
    this.filesService.getRecords()
  }

  onSelectTag(tag: any) {
    console.log(this.filesService.filterRecords(tag[0], tag[1]))
  }
}
