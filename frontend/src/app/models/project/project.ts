import { ProjectRole } from "./roles"


export class NewProject {

    constructor(name: string, description: string) {
        this.name=name
        this.description=description
    }

    name: string
    description: string
}

export class UpdateProject {
    
    constructor(id: number, name: string, description: string) {
        this.id=id
        this.name=name
        this.description=description
    }

    id: number
    name: string
    description: string
}

export class Project {

    constructor(id: number, name: string, description: string, creationDate: Date, closeDate: Date, active: boolean, projectRoles: Array<ProjectRole>) {
        this.id=id
        this.name=name
        this.description=description
        this.creationDate=creationDate
        this.closeDate=closeDate
        this.active=active
        this.projectRoles=projectRoles
    }

    id: number
    name: string
    description: string
    creationDate: Date
    closeDate: Date
    active: boolean
    projectRoles: Array<ProjectRole>
}

