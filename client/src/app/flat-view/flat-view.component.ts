import { Component } from '@angular/core';
import { UserDataService } from '../user-data.service';
import { NgFor, NgIf} from '@angular/common';
import { FilesService } from '../files.service';

@Component({
  selector: 'app-flat-view',
  standalone: true,
  imports: [NgFor, NgIf],
  templateUrl: './flat-view.component.html',
  styleUrl: './flat-view.component.css'
})
export class FlatViewComponent {
  userDataService: UserDataService
  filesService: FilesService

  constructor(userDataService: UserDataService, filesService: FilesService) {
    this.userDataService = userDataService
    this.filesService = filesService
    this.filesService.getFilePaths()
  }

  hasNoFlatFiles() {
    return this.userDataService.parsed.length == 0
  }
  
  onDownload(filename: string) {
    this.filesService.downloadFile(filename)
  }
}
