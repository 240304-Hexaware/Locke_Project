import { Component, OnChanges, SimpleChanges } from '@angular/core';
import { UserDataService } from '../user-data.service';
import { ReqInfoService } from '../req-info.service';
import { NgFor, NgIf, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FilesService } from '../files.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NgFor, NgIf, NgClass, FormsModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  userDataService: UserDataService
  filesService: FilesService
  reqInfoService: ReqInfoService

  specFileId: string = ""

  constructor(userDataService: UserDataService, filesService: FilesService, reqInfoService: ReqInfoService) {
    this.userDataService = userDataService
    this.userDataService.getUserData()
    this.userDataService.getLastHistory()
    this.filesService = filesService
    this.reqInfoService = reqInfoService
  }

  onSpecFileSelect(event: any) {
    const file: File = event.target.files[0]
    this.filesService.uploadSpecFile(file)
    .subscribe({next: (res) => {
      if(res.status == 201) {
        this.userDataService.getUserData()
        this.userDataService.getLastHistory()
      }
    }})
  }

  onUploadAttempt() {
    if (this.specFileId == "") {
      
    }
  }

  flatUploadClick() {
    if(this.specFileId != "") {

    }
  }

  onFlatFileSelect(event: any) {
    if(this.specFileId != "") {
      const file: File = event.target.files[0]
      this.filesService.uploadFlatFile(file, this.specFileId)
      .subscribe({next: (res) => {
        if(res.status == 201) {
          this.userDataService.getUserData()
          this.userDataService.getLastHistory()
        }
      }})
    } else {
      this.reqInfoService.attemptInvalidFlatUpload()
    }
  }

  onDownloadLatest(filename: string) {
    this.filesService.downloadFile(filename)
  }

  seeLastUpload() {
    if(this.userDataService.history?.operation == "postSpec") {
      this.filesService.show = true
      this.filesService.setRawFile("Spec", this.userDataService.history.fileId)
    } else {
      this.filesService.show = true
      this.filesService.setRawFile("Parsed", this.userDataService.history!.fileId)
    }
  }
  
  
}
