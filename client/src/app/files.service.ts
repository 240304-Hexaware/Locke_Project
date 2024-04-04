import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../environments/environment.development';
import { ReqInfoService } from './req-info.service';
import { UserDataService } from './user-data.service';
import { saveAs } from 'file-saver';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FilesService {
  userId: string = ""
  userSpecs: Spec[] | [] = []
  userParsed: Parsed[] | [] = []
  userRecords: Record[] | [] = []
  flatFiles: string[] = []
  recordFilters: UserFilter[] = []
  filteredRecords: Record[] = []

  rawFile: Parsed | Spec | null = null
  rawType: "Parsed" | "Spec" | null = null
  showRawFile = false

  constructor(private http: HttpClient, private router: Router, private infoService: ReqInfoService) { }

  public uploadSpecFile(file: File) {
    let reqData = new FormData()
    reqData.append("file", file)
    reqData.append("specName", file.name)

    return this.http.post(`${environment.apiUrl}/files/spec-file/user/${this.userId}`, reqData, {observe: "response", withCredentials: true})
  }

  public uploadFlatFile(file: File, specId: string) {
    let reqData = new FormData()
    reqData.append("file", file)
    reqData.append("flatFileName", file.name)
    reqData.append("specFileId", specId)

    return this.http.post(`${environment.apiUrl}/files/flat-file/user/${this.userId}`, reqData, {observe: "response", withCredentials: true})
  }

  public downloadFile(filename: string) {
    this.http.get(`${environment.apiUrl}/files/flat-files/file/${filename}/user/${this.userId}`,
    {responseType: 'arraybuffer', withCredentials: true})
    .subscribe({ next: data => saveAs(new Blob([data]), filename)})
  }

 getFilePaths() {
    this.http.get<string[]>(`${environment.apiUrl}/files/flat-files/paths/user/${this.userId}`)
    .subscribe({ next: data => {
      this.flatFiles = data
    }})
  }

  public getSpecs() {
    this.http.get<Spec[]>(`${environment.apiUrl}/files/spec-files/user/${this.userId}`, {withCredentials: true})
    .subscribe({ next: (data) => this.userSpecs = data})
  }

  public getRecords() {
    this.http.get<Record[]>(`${environment.apiUrl}/users/id/${this.userId}/records`, {withCredentials: true})
    .subscribe({ next: (data) => {
      this.userRecords = data
      this.userRecords.forEach(x => {
        if(x.fields.publishDate != undefined) {
         x.fields.publishDate = x.fields.publishDate.split('T')[0]
        }
      })
    }})
  }

  public filterRecords(filter: string, value: any) {
    return this.userRecords.filter(x => x.fields[filter] != undefined && x.fields[filter] == value)
  }

  public getParsedFiles() {
    this.http.get<Parsed[]>(`${environment.apiUrl}/files/parsed-files/user/${this.userId}`, {withCredentials: true})
    .subscribe({ next: (data) =>  this.userParsed = data})
  }

  public setRawFile(fileType: "Spec" | "Parsed", id: string) { 
    if(fileType == "Spec") {
      this.rawType = "Spec"
      this.rawFile = this.userSpecs.filter(spec => spec.id == id)[0]
    } else {
      this.rawType = "Parsed"
      this.rawFile = this.userParsed.filter(parsed => parsed.id == id)[0]
    }
  }

  get getRawType() {
    return this.rawType
  }

  get showRaw() {
    return this.showRawFile
  }

  set show(show: boolean) {
    this.showRawFile = show
  } 

  get specs() {
    return this.userSpecs
  }

  get parsed() {
    return this.userParsed
  }

  applyFilters() {
    return this.recordFilters.length > 0 ? 
    this.userRecords.filter( record => 
      this.recordFilters.some(filter =>
        filter.name == "id" ? (filter.values.length != 0 && filter.values.includes(record.id)) 
        : filter.values.includes(record.fields[filter.name])
      )
    )
    : this.userRecords
  }

  get records() {
    return this.filteredRecords.length > 0 ? this.filteredRecords : this.userRecords
  }

  get hasRecords() {
    return this.userRecords.length > 0
  }

  get hasSpecs() {
    return this.userSpecs.length > 0
  }

  get hasParsed() {
    return this.userParsed.length > 0
  }

  getFlats() {
    return this.flatFiles
  }

  set setUserId(userId: string) {
    this.userId = userId
  }

  set filterCriteria(filters: UserFilter[]) {
    this.recordFilters = filters
  }

  get raw() {
    return this.rawFile
  }

  formatThumbUrl(key: string) {
    return `/assets/images/thumbs/${key}.jpg`
  }

  getTags(record: Record) {
    return Object.entries(record.fields).filter(x => x[0] != "thumbUrl")
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

interface Record {
  id: string
  fields: any
}

interface UserFilter {
  name: string
  values: any[]
}