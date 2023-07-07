import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { menuGroups } from 'src/app/models/menu/menu';
import { Project } from 'src/app/models/project/project';
import { TokenService } from 'src/app/services/authentication/token.service';
import { ProjectService } from 'src/app/services/project/project.service';
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

  myProjects: Array<Project> = []

  constructor(private tokenService: TokenService, private userService: UserService, private projectService: ProjectService, private router: Router) { }

  ngOnInit(): void {
    this.username = this.tokenService.getUsername()
    this.imageUrl = this.userService.imageUrl

    this.loadMyProjects()
  }

  loadMyProjects() {
    this.projectService.myProjects().subscribe({
      next: (n) => {
          this.myProjects = n
      },
      error: (e) => {
        console.error(e)
      }
    })
  }

  onLogout(): void {
    this.tokenService.logOut();
    this.router.navigate(["/login"])
  }

  reloadWindow() {
    window.location.reload()
  }

}
