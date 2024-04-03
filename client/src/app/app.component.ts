import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SignUpFormComponent as SignUpFormComponent } from './sign-up-form/sign-up-form.component';
import { UserListComponent } from './user-list/user-list.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { RecordsViewComponent } from './records-view/records-view.component';
import { NotificationComponent } from './notification/notification.component';
import { RawViewComponent } from './raw-view/raw-view.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SignUpFormComponent, UserListComponent, NavBarComponent, 
    RecordsViewComponent, NotificationComponent, RawViewComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

}
