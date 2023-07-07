export class UserShowDto {

    username: string
    firstName: string
    lastName: string
    email: string
    imageUid: string

    constructor(username: string, firstName: string, lastName: string, email: string, imageUid: string) {
        this.username = username
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.imageUid = imageUid
    }
}
