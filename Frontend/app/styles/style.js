import { StyleSheet } from "react-native"; 

export const styles = StyleSheet.create({
    container: {
        fontSize: 30,
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'white'
    },
    button: {
        backgroundColor: '#A08CFF',
        paddingVertical: 15,
        paddingHorizontal: 30,
        borderRadius: 10,
        marginBottom: 20,
        width: '60%',
        alignItems: 'center',
    },
    buttext: {
        color: 'white',
        fontSize: 16,
        fontFamily: 'Paperlogy-6SemiBold',
    },
    profileText: {
        fontSize: 16,
        color: '#7A5ADB',
        fontFamily: 'Paperlogy-6SemiBold',
    },
    logoutButton: {
        backgroundColor: '#FF4848',
        paddingVertical: 7,
        paddingHorizontal: 30,
        borderRadius: 10,
        marginBottom: 20,
        width: '40%',
        alignItems: 'center',
    },
    logoutText: {
        fontSize: 15,
        color: 'white',
        fontFamily: 'Paperlogy-6SemiBold',
    },
    mainView: {
        marginBottom: 20, 
        width: '80%'
    },
    input: {
        fontSize: 20,
        padding: 10,
        width: '100%',
        backgroundColor: '#DDDDDD',
        marginBottom: 20,
    },
    profileTextInput: {
        marginBottom: 20,
        backgroundColor: 'white',
        paddingHorizontal: 5,
        height: 40,
    },
    image: {
        width: 100,
        height: 100,
        marginBottom: 40,
        marginTop: 40,
        borderRadius: 100,
        backgroundColor: '#CCCCCC'
    },
    loginImage: {
        width: 150,
        height: 150,
        marginTop: 10,
        borderRadius: 100,
        backgroundColor: '#CCCCCC'
    },
    background: {
        width: '100%',
        height: '100%',
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center'
    },
    mainText: {
        fontFamily: 'Paperlogy-7Bold',
        fontSize: 40,
        marginBottom: 20,
        color: '#A08CFF',
    },
    textInput: {
        marginBottom: 20,
        backgroundColor: 'white',
    },
    text: {
        fontSize: 15,
        fontWeight: 'bold',
        color: 'black',
    },
    joinText: {
        fontSize: 15,
        fontFamily: 'Paperlogy-6SemiBold',
        color: 'black',
    }
}); 