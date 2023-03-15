import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { menuGroups } from 'src/app/models/menu/menu';
import { TokenService } from 'src/app/services/authentication/token.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  username: any
  imageUrl: any
  menuGroups = menuGroups;

  constructor(private tokenService: TokenService, private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.username = this.tokenService.getUsername()
    this.imageUrl = this.userService.imageUrl
  }

  onLogout(): void {
    this.tokenService.logOut();
    this.router.navigate(["/login"])
  }

  reloadWindow() {
    window.location.reload()
  }

}
