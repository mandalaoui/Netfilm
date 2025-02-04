import React, { useEffect , useState } from 'react';
import { useNavigate } from 'react-router-dom';

export function AuthGuard({ children }) {
    const navigate = useNavigate();
    const [error, setError] = useState('');

    useEffect(() => {
        const token = localStorage.getItem('authToken');
        if (!token) {
            setError('You need to log in to access this page.');
            navigate('/login');
        }
    }, [navigate]);

    return (
        <>
            {error && <div className="error-message">{error}</div>}
            {children}
        </>
    );
}

export function AdminGuard({ children }) {
    const navigate = useNavigate();
    const [error, setError] = useState('');

    useEffect(() => {
        const token = localStorage.getItem('authToken');
        const isAdmin = localStorage.getItem('isAdmin');
        if (!token) {
            setError('You need to log in to access this page.');
            navigate('/login');
            return;
        }
        if (isAdmin !== 'true') {
            setError('You do not have the required permissions to access this page.');
            navigate('/home');
        }
    }, [navigate]);

    return (
        <>
            {error && <div className="error-message">{error}</div>}
            {children}
        </>
    );
}
