import { Component } from '@angular/core';
import { UserDataService } from '../user-data.service';
import { NgFor, KeyValuePipe, NgIf } from '@angular/common';
import { FilesService } from '../files.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-parsed-view',
  standalone: true,
  imports: [NgFor, NgIf, KeyValuePipe, RouterLink],
  templateUrl: './parsed-view.component.html',
  styleUrl: './parsed-view.component.css'
})
export class ParsedViewComponent {
  filesService: FilesService
  userDataService: UserDataService

  constructor(filesService: FilesService, userDataService: UserDataService) {
    this.filesService = filesService
    this.userDataService = userDataService
    this.filesService.getParsedFiles()
  }

  showParsed(id: string) {
    this.filesService.setRawFile("Parsed", id)
    this.filesService.show = true
  }

  setFilterCriteria(ids: string[]) {
    this.filesService.recordFilters = [{name: "id", values: ids}]
    this.filesService.filteredRecords = this.filesService.applyFilters()
  }
}
