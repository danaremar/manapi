import { Injectable } from "@angular/core"
import { HttpClient } from '@angular/common/http'
import { environment } from "src/environments/environment"
import { Observable } from "rxjs"
import { NewProject, Project, UpdateProject } from "src/app/models/project/project"
import { CreateProjectRole, NotAcceptedProjectRole, UpdateProjectRole } from "src/app/models/project/roles"

const PROJECT_ID = 'ProjectId'
const PROJECTS = 'Projects'

@Injectable({
    providedIn: 'root'
})
export class ProjectService {

    // URL
    hostUrl = environment.backendEndpoint + '/project/'

    // CONSTRUCTOR
    constructor(private httpClient: HttpClient) { }

    /*
    *
    * OPERATIONS
    * 
    */

    public myProjects(): Observable<any> {
        let url = this.hostUrl + 'all'
        return this.httpClient.get<Project[]>(url)
    }

    public getStoredProjectId(): number | null {
        return Number(localStorage.getItem(PROJECT_ID))
    }

    public setStoredProjectId(projectId: number): void {
        localStorage.setItem(PROJECT_ID,String(projectId))
    }

    public getStoredProjects(): Array<Project> {
        let a = localStorage.getItem(PROJECTS)
        if(a!=null) {
            return JSON.parse(a)
        }
        return []
    }

    public setStoredProjects(projects: Array<Project>) {
        localStorage.setItem(PROJECTS, JSON.stringify(projects))
    }

}