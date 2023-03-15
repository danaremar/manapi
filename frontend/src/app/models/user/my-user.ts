export class MyUser {

    username: string
    name: string
    lastName: string
    email: string
    country: string
    imageUid: string

    constructor(username: string, name: string, lastName: string, email: string, country: string, imageUid: string) {
        this.username = username
        this.name = name
        this.lastName = lastName
        this.email = email
        this.country = country
        this.imageUid = imageUid
    }
}