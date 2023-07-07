import { UserShowDto } from "../user/show-user"

export class ProjectRole {
    constructor(id: number, user: UserShowDto, role: string, accepted: boolean) {
        this.id = id
        this.user = user
        this.role = role
        this.accepted = accepted
    }
    id: number
    user: UserShowDto
    role: string
    accepted: boolean
}

export class CreateProjectRole {
    constructor(projectId: number, username: string, role: string) {
        this.projectId = projectId
        this.username = username
        this.role = role
    }
    projectId: number
    username: string
    role: string
}

export class UpdateProjectRole {
    constructor(id: number, role: string) {
        this.id = id
        this.role = role
    }
    id: number
    role: string
}

export class NotAcceptedProjectRole {
    constructor(id: number, role: string, projectName: string) {
        this.id = id
        this.role = role
        this.projectName = projectName
    }
    id: number
    role: string
    projectName: string
}