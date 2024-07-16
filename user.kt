class user {
    private var id: Int =0
        private var username:String ?=null
        private var surname:String ?=null
        private var email:String ?=null
        private var contact:Int=0

        constructor(username: String, surname: String, email:String, contact: Int){
            this.username=username
            this.surname=surname
            this.email=email
            this.contact=contact
        }

        /*fun getUsername(): String {
            return username
        }
        fun getPreNom(): String {
            return surname
        }
        fun getEmail(): String {
            return email
        }
        fun getContact(): Int {
            return contact
        }*/


}