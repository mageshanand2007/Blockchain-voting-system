// app.js - Mock Backend Simulation using LocalStorage

const DEMO_VOTERS = {
    'VOTER001': { id: 'VOTER001', name: 'Alice', password: 'password', role: 'VOTER', hasVoted: false },
    'VOTER002': { id: 'VOTER002', name: 'Bob', password: 'password', role: 'VOTER', hasVoted: false },
    'VOTER003': { id: 'VOTER003', name: 'Charlie', password: 'password', role: 'VOTER', hasVoted: false }
};

const DEMO_CANDIDATES = [
    { id: 'CAND001', name: 'Candidate A', party: 'Digital Future Party', desc: 'Focused on technological advancement.', symbol: '⭐' },
    { id: 'CAND002', name: 'Candidate B', party: 'People First', desc: 'Prioritizing social welfare.', symbol: '⭕' },
    { id: 'CAND003', name: 'Candidate C', party: 'Future Citizens', desc: 'Building a sustainable future.', symbol: '🌿' }
];

// Initialize Database
function initDB() {
    if (!localStorage.getItem('chainvote_init')) {
        localStorage.setItem('users', JSON.stringify({
            'admin': { id: 'admin', password: 'admin123', role: 'ADMIN' },
            ...DEMO_VOTERS
        }));
        
        localStorage.setItem('candidates', JSON.stringify(DEMO_CANDIDATES));
        
        localStorage.setItem('election_state', JSON.stringify({ isLive: false, results: {} }));
        
        // Genesis Block
        const genesis = {
            index: 0,
            timestamp: Date.now(),
            previousHash: '00000000000000000000000000000000',
            txId: 'GENESIS_BLOCK',
            hash: generateHash('0' + Date.now() + 'GENESIS_BLOCK')
        };
        localStorage.setItem('blockchain', JSON.stringify([genesis]));
        
        localStorage.setItem('chainvote_init', 'true');
    }
}

// Simple Hash Function Simulation
function generateHash(str) {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
        const char = str.charCodeAt(i);
        hash = ((hash << 5) - hash) + char;
        hash = hash & hash;
    }
    return Math.abs(hash).toString(16).padStart(32, '0') + Math.random().toString(16).substr(2, 16);
}

// Authentication
function login(id, password) {
    const users = JSON.parse(localStorage.getItem('users'));
    if (users[id] && users[id].password === password) {
        sessionStorage.setItem('currentUser', JSON.stringify(users[id]));
        return { success: true, user: users[id] };
    }
    return { success: false, message: 'Invalid ID or Password' };
}

function logout() {
    sessionStorage.removeItem('currentUser');
    window.location.href = 'index.html';
}

function getCurrentUser() {
    const userStr = sessionStorage.getItem('currentUser');
    return userStr ? JSON.parse(userStr) : null;
}

function checkAuth(roleRequired) {
    const user = getCurrentUser();
    if (!user) {
        window.location.href = 'login.html';
        return null;
    }
    if (roleRequired && user.role !== roleRequired) {
        window.location.href = 'index.html';
        return null;
    }
    return user;
}

// Election Functions
function startElection() {
    const state = JSON.parse(localStorage.getItem('election_state'));
    state.isLive = true;
    localStorage.setItem('election_state', JSON.stringify(state));
}

function endElection() {
    const state = JSON.parse(localStorage.getItem('election_state'));
    state.isLive = false;
    localStorage.setItem('election_state', JSON.stringify(state));
}

function isElectionLive() {
    return JSON.parse(localStorage.getItem('election_state')).isLive;
}

function castVote(voterId, candidateId) {
    if (!isElectionLive()) return { success: false, message: 'Election is closed.' };
    
    const users = JSON.parse(localStorage.getItem('users'));
    if (users[voterId].hasVoted) return { success: false, message: 'You have already voted.' };
    
    // Process Vote
    users[voterId].hasVoted = true;
    localStorage.setItem('users', JSON.stringify(users));
    
    // Add to Block
    const txId = 'TX' + Math.random().toString(36).substr(2, 9).toUpperCase();
    const chain = JSON.parse(localStorage.getItem('blockchain'));
    const prevBlock = chain[chain.length - 1];
    
    const newBlock = {
        index: chain.length,
        timestamp: Date.now(),
        previousHash: prevBlock.hash,
        txId: txId,
        hash: generateHash(chain.length + Date.now().toString() + prevBlock.hash + txId)
    };
    chain.push(newBlock);
    localStorage.setItem('blockchain', JSON.stringify(chain));
    
    // Update Results
    const state = JSON.parse(localStorage.getItem('election_state'));
    if (!state.results[candidateId]) state.results[candidateId] = 0;
    state.results[candidateId]++;
    localStorage.setItem('election_state', JSON.stringify(state));
    
    // Update current user session
    sessionStorage.setItem('currentUser', JSON.stringify(users[voterId]));
    
    return { success: true, txId: txId, block: newBlock };
}

initDB();
