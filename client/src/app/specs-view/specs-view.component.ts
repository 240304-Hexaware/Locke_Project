import { Component } from '@angular/core';
import { UserDataService } from '../user-data.service';
import { KeyValuePipe, NgFor, NgIf } from '@angular/common';
import { FilesService } from '../files.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-specs-view',
  standalone: true,
  imports: [NgFor, NgIf, KeyValuePipe, RouterLink],
  templateUrl: './specs-view.component.html',
  styleUrl: './specs-view.component.css'
})
export class SpecsViewComponent {
  userDataService: UserDataService
  filesService: FilesService

  constructor (userDataService: UserDataService, filesService: FilesService) {
    this.userDataService = userDataService
    this.filesService = filesService
    this.filesService.getSpecs()
  }

  showSpec(specId: string) {
    this.filesService.setRawFile("Spec", specId)
    this.filesService.show = true
  }

  stringifySpec(spec: any): String {
    return JSON.stringify(spec, null, 2)
  }
  
  setFilterCriteria(recordIds: string[]) {
    this.filesService.filterCriteria = [{name: "id", values: recordIds}]
  }

}