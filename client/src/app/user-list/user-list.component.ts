import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent {
  url = "http://localhost:8080/api/v1/users"
  
  users: User[] = []

  constructor() {
    this.getAllUsers()
      .then( usersData => this.users = usersData)
  }

  async getAllUsers() : Promise<User[]> {
    const data = await fetch(this.url)
    return await data.json() ?? []
  }
}

interface User {
  id: number
  username: string
  password: string
  role: string
}