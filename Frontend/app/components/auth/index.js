import React, { useEffect } from 'react';
import {StyleSheet, Text, View, ActivityIndicator, ScrollView} from 'react-native';
import AuthForm from './authForm';
import { getTokens } from '../../utils/misc';

// const AuthStack = createStackNavigator();
const AuthComponent = ({navigation}) => {

    const [loading, setLoading] = React.useState(false)

    goWithoutLogin = () => {
        navigation.navigate("AppTabComponent")
    }

    useEffect(() => {
        getTokens();
      }, []);
    
    if(loading) {
        return (
            <View style={styles.loading}>
                <ActivityIndicator/>
            </View>
        )
    }
    else {
        return (
            <View style={styles.container}>
                <AuthForm
                    navigation={navigation}
                />
            </View>
            
        )
    }
    
};

const styles = StyleSheet.create({
    loading: {
        flex: 1,
        backgroundColor: 'white',
        justifyContent: 'center',
        alignItems: 'center',
    },
    container: {
        flex: 1,
        backgroundColor: 'white',
    }
});

export default AuthComponent;
