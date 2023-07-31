import { Component, OnInit, Renderer2 } from '@angular/core';
import { menuGroups } from 'src/app/models/menu/menu';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  // THEME
  theme: string = "dark"        // selected theme: dark / light
  opt_theme: string = "system"  // user option theme: dark / light / system

  // TODO: user profile
  username: any = 'danaremar'
  imageUrl: any = '/assets/cute-astronaut-1.jpg'

  // MENU (from menu.ts)
  menuGroups = menuGroups;

  // PROJECTS
  selProject: any
  selProjectId: any
  myProjects: Array<any> = []

  constructor(private renderer: Renderer2) {

  }

  ngOnInit(): void {
    this.detectSystemTheme()
  }

  detectSystemTheme() {
    const darkThemeQuery = window.matchMedia('(prefers-color-scheme: dark)')
    if (darkThemeQuery.matches) {
      this.theme = 'dark'
    } else {
      this.theme = 'light'
    }
    this.changeTheme(this.theme)
  }

  switchTheme() {
    if (this.theme === 'dark') {
      this.changeTheme('light')
    } else {
      this.changeTheme('dark')
    }
  }

  changeTheme(theme: string) {
    const body = document.querySelector('html');
    this.renderer.removeAttribute(body, "data-bs-theme")
    this.theme = theme
    this.renderer.setAttribute(body, "data-bs-theme", theme)
  }

}
