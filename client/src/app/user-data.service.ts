import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import { Router } from '@angular/router';
import { ReqInfoService } from './req-info.service';
import { FilesService } from './files.service';

interface User {
  id: string
  username: string
  role: Role[]
  createdAt: Date | null
  specFileIds: string[]
  parsedFileIds: string[]
  recordIds: string[]
}

interface Role {
  id: string
  authority: string
}

@Injectable({
  providedIn: 'root'
})
export class UserDataService {
  filesService: FilesService
  reqInfoService: ReqInfoService
  userData: User
  lastHistory: MetaTag | null = null
  userHistory: MetaTag[] = []
  jwtToken: string | null = null

  constructor(private http: HttpClient, private router: Router, filesService: FilesService, reqInfoService: ReqInfoService) {
    this.userData = {id: "", username: "", role: [], createdAt: null, specFileIds: [], parsedFileIds: [], recordIds: []}
    this.filesService = filesService
    this.reqInfoService = reqInfoService
  }

  signup(userData: {username: string, password: string, role: string}) {
    this.http.post<User>(`${environment.apiUrl}/auth/register`, {
      username: userData.username, password: userData.password, role: userData.role
    }, {observe: "response", withCredentials: true})
    .subscribe({next: res => {
      if(res.status == 400) {

      } else if(res.body) {
        if(res.headers.get('authorization')) {
            this.jwtToken = res.headers.get('authorization')
        }
        this.userData = res.body
        this.filesService.setUserId = res.body.id
        this.filesService.getRecords()
        this.filesService.getSpecs()
        this.getLastHistory()

        this.router.navigate(['home'])
      }
    }})
  }

  login(userData: {username: string, password: string}) {
    return this.http.post<User>(`${environment.apiUrl}/auth/login`, {
      username: userData.username,
      password: userData.password
    }, {
      observe: "response",
      withCredentials: true,
    })
    .subscribe({
      next: (res) => {
        if(res.body) {
          if(res.headers.get('authorization')) {
            this.jwtToken = res.headers.get('authorization')
          }

          this.userData = res.body
          this.filesService.setUserId = res.body.id
          this.filesService.getRecords()
          this.filesService.getSpecs()
          this.getLastHistory()

          this.router.navigate(['home'])
        }
      }
    })
  }

  getUserData() {
    if(this.hasUser()) {
      this.http.get<User>(`${environment.apiUrl}/users/id/${this.userData.id}`, {withCredentials: true})
      .subscribe({ next: (data) => { 
        this.userData = data 
      }})
    }
  }

  logout() {
    this.reqInfoService.logInSucceeded()
    this.userData = {id: "", username: "", role: [], createdAt: null, specFileIds: [], parsedFileIds: [], recordIds: []}
    this.jwtToken = null
  }

  public getLastHistory() {
    if(this.hasUser()) {
      this.http.get<MetaTag>(`${environment.apiUrl}/files/history/last/${this.userData.id}`, {withCredentials: true})
      .subscribe({ next: (data) => {
        this.lastHistory = data
      }})
    }
  }

  public getHistory() {
    if(this.hasUser()) {
      this.http.get<MetaTag[]>(`${environment.apiUrl}/files/history/${this.userData.id}`, {withCredentials: true})
      .subscribe({ next: (data) => {
        this.userHistory = data
      }})
    }
  }

  get specs() {
    return this.userData.specFileIds
  }

  get parsed() {
    return this.userData.parsedFileIds
  }

  get records() {
    return this.userData.recordIds
  }

  public get username() : string {
    return this.userData.username
  }

  public isAdmin(): boolean {
    return this.userData.role.filter(x => x.authority == "ADMIN").length == 1
  }

  public get createdAt(): string {
    return this.userData.createdAt ? this.userData.createdAt?.toString().split("T")[0].replaceAll("-", "/") 
    : ""
  }

  public get hasSpecs(): boolean {
    return this.userData.specFileIds.length > 0
  }

  public get history() {
    return this.lastHistory
  }

  public get allHistory() {
    return this.userHistory
  }

  formatThumbUrl(key: string) {
    return `/assets/images/thumbs/${key}.jpg`
  }

  hasUser(): boolean {
    return this.userData.id ? true : false
  }
}

interface MetaTag {
  id: string
  userId: string
  createdAt: string
  operation: string
  fileName: string
  fileId: string
  recordsCreated: string[]
}