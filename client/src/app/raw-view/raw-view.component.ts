import { Component } from '@angular/core';
import { FilesService } from '../files.service';
import { NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-raw-view',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './raw-view.component.html',
  styleUrl: './raw-view.component.css'
})
export class RawViewComponent {
  filesService: FilesService

  constructor(filesService: FilesService) {
    this.filesService = filesService
  }

  get rawParsed() {
    if(this.filesService.rawType == "Parsed") {
      return this.filesService.raw as Parsed
    } else return null 
  }

  get rawSpec() {
    if (this.filesService.rawType == "Spec") {
      return this.filesService.raw as Spec
    } else return null
  }

  get rawSpecJSON() {
    return JSON.stringify(this.rawSpec, null, 2)
  }

  get rawParsedJSON() {
    return JSON.stringify(this.rawParsed, null, 2)
  }

  close() {
    this.filesService.show = false
  }

}

interface Spec {
  id: string
  userId: string
  name: string
  fields: Object[]
  parsedFileIds: string[]
  recordIds: string[]
  createdAt: string
}

interface Parsed {
  id: string
  name: string
  recordIds: string[]
  createdAt: string
}